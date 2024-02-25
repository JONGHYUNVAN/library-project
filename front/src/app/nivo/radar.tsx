import { ResponsiveRadar } from '@nivo/radar'
interface ChartData {
    [key: string]: number | string;
}

type ChartDataArray = ChartData[];

export const MyResponsiveRadar = ({ data }: { data: ChartDataArray }) => (
    <ResponsiveRadar
        enableDots={true}
        isInteractive={true}
        dotLabel="value"
        dotLabelYOffset={-12}
        data={data}
        keys={[ 'searched', 'loaned' ]}
        indexBy="genre"
        fillOpacity={1}
        valueFormat=">-.2f"
        margin={{ top: 70, right: 80, bottom: 40, left: 80 }}
        borderColor={{ from: 'color' }}
        gridLabelOffset={36}
        dotSize={10}
        dotColor={{ theme: 'labels.text.fill' }}
        dotBorderWidth={1}
        colors={["#ddaa00", "#cc7700", "#ffffff", "#ffcc86"]}
        theme={{
            tooltip: {
                container: {
                    background: 'rgba(230, 180, 30, 0.3)',
                    color: '#ffcc86'
                },
            },
            text: {
                fill: "#ffcc86",
                fontSize: "clamp(10px, 2vw, 20px)",
            },
        }}
        blendMode="lighten"
        motionConfig="wobbly"
        legends={[
            {
                anchor: 'top-left',
                direction: 'column',
                translateX: -50,
                translateY: -40,
                itemWidth: 40,
                itemHeight: 20,
                itemTextColor: '#ffcc88',
                symbolSize: 12,
                symbolShape: 'circle',
                effects: [
                    {
                        on: 'hover',
                        style: {
                            itemTextColor: '#ffcc88'
                        }
                    }
                ]
            }
        ]}
    />
)